package com.example.s3rekognition.controller;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.s3rekognition.PPEClassificationResponse;
import com.example.s3rekognition.PPEResponse;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class RekognitionController implements ApplicationListener<ApplicationReadyEvent> {

    private final AmazonS3 s3Client;
    private final AmazonRekognition rekognitionClient;
    private final MeterRegistry meterRegistry;
    private final Counter tankDetectedCounter;

    private static final Logger logger = Logger.getLogger(RekognitionController.class.getName());
    

    @Autowired
    public RekognitionController(MeterRegistry meterRegistry) {
        this.s3Client = AmazonS3ClientBuilder.standard().build();
        this.rekognitionClient = AmazonRekognitionClientBuilder.standard().build();
        this.meterRegistry = meterRegistry;
        this.tankDetectedCounter = meterRegistry.counter("tank-detected-metric", "resource", "image");
    }

    /**
     * Vernevokterene got a new board of directors who lives by the statement: Cash is king!
     * When they figured out that there werent alot of money in the public health sector, they
     * decided to look into the private construction sector, and with great sucsess! Therefore they asked 
     * the developer (me) to restucture our code to focus on construction site ppe instead of in the health sector.
     * Changed the app to be target towards a construction site! Lets assume the images in the
     * bucket are from a security camera at the site, and it scans pictures daily to ensure
     * safety and good practice at the construction site! Its also used by security to see
     * if any workers or guests try to smuggel in some illegal substances or weapons!
     */

    @GetMapping(value = "/scan-ppe", consumes = "*/*", produces = "application/json")
    @ResponseBody
    public ResponseEntity<PPEResponse> scanForPPE(@RequestParam String bucketName) {
        // Add a timer to see how fast our system can registrer faults in use of hard-hats.
        Timer.Sample sample = Timer.start(meterRegistry);
        
        //Check have many bad or corrupted images in bucket.        
        Counter nonJpegImagesCounter = meterRegistry.counter("non-jpeg-images-count", "bucket", bucketName);
        
        //Total files scanned.
        Counter allFilesScannedCounter = meterRegistry.counter("all-files-scanned-count", "bucket", bucketName);

        // List all objects in the S3 bucket
        ListObjectsV2Result imageList = s3Client.listObjectsV2(bucketName);

        // This will hold all of our classifications
        List<PPEClassificationResponse> classificationResponses = new ArrayList<>();

        // This is all the images in the bucket
        List<S3ObjectSummary> images = imageList.getObjectSummaries();

        // Iterate over each object and scan for PPE
        for (S3ObjectSummary image : images) {
            String fileName = image.getKey();
            
            allFilesScannedCounter.increment();
            
            if (!(fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg"))) {
            // Increment the counter for non-JPEG/JPG images
            nonJpegImagesCounter.increment();
            continue; 
            }
            
            if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
                logger.info("scanning " + fileName);

                // This is where the magic happens, use AWS rekognition to detect PPE
                DetectProtectiveEquipmentRequest request = new DetectProtectiveEquipmentRequest()
                        .withImage(new Image()
                                .withS3Object(new S3Object()
                                        .withBucket(bucketName)
                                        .withName(image.getKey())))
                        .withSummarizationAttributes(new ProtectiveEquipmentSummarizationAttributes()
                                .withMinConfidence(80f)
                                .withRequiredEquipmentTypes("HEAD_COVER"));
                DetectProtectiveEquipmentResult result = rekognitionClient.detectProtectiveEquipment(request);

                // If any person on an image lacks PPE on the face, it's a violation of regulations
                boolean violation = isViolation(result);

                logger.info("scanning " + image.getKey() + ", violation result " + violation);
                // Categorize the current image as a violation or not.
                int personCount = result.getPersons().size();
                PPEClassificationResponse classification = new PPEClassificationResponse(image.getKey(), personCount, violation);
                classificationResponses.add(classification);
            }
        }
        
        sample.stop(meterRegistry.timer("ppe-scan-timer", "bucket", bucketName));
        
        PPEResponse ppeResponse = new PPEResponse(bucketName, classificationResponses);
        return ResponseEntity.ok(ppeResponse);   
    }
    
    /**
     * Assuming that the primary use of this service is at a construction site, it is nice for security
     * to be able to see if any old employes are coming back to site with malicious intentions!
     * Therefore a scan to see if anyone has any dangerous items at their person is a good safety
     * feature that also can perhaps trigger an alert! :)
     */
    @GetMapping("/scan-sri")
    public ResponseEntity<String> detectSecurityRiskItems(@RequestParam String bucketName) {
        // A good security company needs to be able to respons to treath fast! therefore a timer
        // to asses how fast we can detect danger, and in turn act on them, is crucial!
        Timer.Sample sample = Timer.start(meterRegistry);
        
        // Counts all dangerous items found at the site.
        Counter dangerousItemsCounter = meterRegistry.counter("security-items-detection-count", "bucket", bucketName);
        Counter imagesScannedCounter = meterRegistry.counter("security-items-scanned", "bucket", bucketName);
        Counter safeWorkersCounter = meterRegistry.counter("safe-workers-detected", "bucket", bucketName);
        
        // List all objects in the S3 bucket
        ListObjectsV2Result imageList = s3Client.listObjectsV2(bucketName);
    
        // This will hold all of our detected items
        List<String> dangerousItems = new ArrayList<>();
    
        // This is all the images in the bucket
        List<S3ObjectSummary> images = imageList.getObjectSummaries();
            
        for (S3ObjectSummary image : images) {
            String fileName = image.getKey();
            if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
                logger.info("Scanning " + fileName + " for dangerous items");
                imagesScannedCounter.increment();
                
                DetectLabelsRequest request = new DetectLabelsRequest()
                        .withImage(new Image()
                                .withS3Object(new S3Object()
                                        .withBucket(bucketName)
                                        .withName(fileName)))
                        .withMaxLabels(10)
                        .withMinConfidence(75F);
    
                DetectLabelsResult result = rekognitionClient.detectLabels(request);
                boolean tankDetected = result.getLabels().stream()
                .anyMatch(label -> label.getName().equals("Tank"));
                
                if (tankDetected) {
                tankDetectedCounter.increment();
                logger.info("Tank detected in " + fileName);
                }
    
                List<String> itemsInImage = result.getLabels().stream()
                    .filter(label ->
                    label.getName().equals("Gun") || 
                    label.getName().equals("Knife") ||
                    label.getName().equals("Sword") ||
                    label.getName().equals("Tank")
                    )
                    .map(Label::getName)
                    .collect(Collectors.toList());
    
                if (!itemsInImage.isEmpty()) {
                    dangerousItems.addAll(itemsInImage);
                    dangerousItemsCounter.increment(itemsInImage.size());
                    logger.info("Found dangerous items in " + fileName + ": " + itemsInImage);
                } 
                else {
                    safeWorkersCounter.increment();
                }
                
                    
            }
        }
        
        sample.stop(meterRegistry.timer("sri-scan-timer", "bucket ", bucketName));
        
        return ResponseEntity.ok("Found " + dangerousItems.size() +
                                 " dangerous items in todays photos:" + "\n" + dangerousItems);
    }
    
    /**
     * And while we are at it, the war on drugs most continue! This method checks if any 
     * illegal substances or alcohol have been taken into the workplace! A sober workplace
     * is a safe one!
     */
    @GetMapping("/scan-srs")
    public ResponseEntity<String> detectSecurityRiskSubstances(@RequestParam String bucketName) {
        // List all objects in the S3 bucket
        ListObjectsV2Result imageList = s3Client.listObjectsV2(bucketName);
    
        // This will hold all of our detected items
        List<String> dangerousSubstance = new ArrayList<>();
    
        // This is all the images in the bucket
        List<S3ObjectSummary> images = imageList.getObjectSummaries();
            
        for (S3ObjectSummary image : images) {
            String fileName = image.getKey();
            if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
                logger.info("Scanning " + fileName + " for dangerous substances");
    
                DetectLabelsRequest request = new DetectLabelsRequest()
                        .withImage(new Image()
                                .withS3Object(new S3Object()
                                        .withBucket(bucketName)
                                        .withName(fileName)))
                        .withMaxLabels(10)
                        .withMinConfidence(75F);
    
                DetectLabelsResult result = rekognitionClient.detectLabels(request);
    
                List<String> itemsInImage = result.getLabels().stream()
                    .filter(label ->
                    label.getName().equals("Beer") || 
                    label.getName().equals("Alcohol") ||
                    label.getName().equals("Smoke Pipe") ||
                    label.getName().equals("Pill")
                    )
                    .map(Label::getName)
                    .collect(Collectors.toList());
                //Sjekk hvilke farlige substanser som oftest ankommer arbeidsplassen for å gjøre 
                //forebyggende tiltak!
                result.getLabels().stream()
                    .filter(label -> Arrays.asList("Beer", "Alcohol", "Smoke Pipe", "Pill").contains(label.getName()))
                    .forEach(label -> {
                        Counter counter = meterRegistry.counter("security-substances-detected", "type", label.getName());
                        counter.increment();
                    });    
            
                if (!itemsInImage.isEmpty()) {
                    dangerousSubstance.addAll(itemsInImage);
                    logger.info("Found dangerous items in " + fileName + ": " + itemsInImage);
                }
            }
        }
        return ResponseEntity.ok("Found " + dangerousSubstance.size() +
                                 " dangerous substances in todays photos:" + "\n" + dangerousSubstance);
    }
    
    private static boolean isViolation(DetectProtectiveEquipmentResult result) {
        return result.getPersons().stream()
                .flatMap(p -> p.getBodyParts().stream())
                .anyMatch(bodyPart -> bodyPart.getName().equals("FACE")
                        && bodyPart.getEquipmentDetections().isEmpty());
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

    }
}
