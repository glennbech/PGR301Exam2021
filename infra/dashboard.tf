resource "aws_cloudwatch_dashboard" "main" {
  dashboard_name = "${var.candidate_name}-dashboard"
  dashboard_body = <<SEC
{
  "widgets": [
    {
      "type": "metric",
      "height": 6,
      "width": 8,
      "y": 0,
      "x": 6,
      "properties": {
          "metrics": [
              [ "${var.namespace}", "security-items-scanned.count", "bucket", "kand2021imagebucket", { "region": "eu-west-1", "label": "Worker without weapons", "color": "#98df8a" } ],
              [ "${var.namespace}", "safe-workers-detected.count", "bucket", "kand2021imagebucket", { "region": "eu-west-1", "label": "Workers with weapons", "color": "#8c564b" } ]
          ],
          "view": "pie",
          "region": "eu-west-1",
          "title": "Workers without compared to workers with weapons",
          "period": 300,
          "setPeriodToTimeRange": true,
          "sparkline": false,
          "trend": false,
          "stat": "Sum",
          "stacked": true,
          "yAxis": {
              "left": {
                  "showUnits": false
              },
              "right": {
                  "showUnits": true
              }
          },
          "labels": {
              "visible": true
          }
      }
    },
    {
      "type": "metric",
      "height": 6,
      "width": 6,
      "y": 0,
      "x": 0,
      "properties": {
          "metrics": [
              [ "${var.namespace}", "security-substances-detected.count", "type", "Smoke Pipe", { "region": "eu-west-1", "yAxis": "left", "color": "#aec7e8" } ],
              [ "${var.namespace}", "security-substances-detected.count", "type", "Pill", { "region": "eu-west-1", "color": "#ffbb78" } ],
              [ "${var.namespace}", "security-substances-detected.count", "type", "Beer", { "region": "eu-west-1", "color": "#98df8a" } ],
              [ "${var.namespace}", "security-substances-detected.count", "type", "Alcohol", { "region": "eu-west-1", "color": "#ff9896" } ]
          ],  
          "view": "bar",
          "region": "eu-west-1",
          "title": "Spread of different substances taken into the workplace",
          "period": 300,
          "setPeriodToTimeRange": true,
          "sparkline": false,
          "trend": false,
          "liveData": true,
          "legend": {
              "position": "right"
          },
          "stat": "Sum"
      }
    },
    {
      "type": "metric",
      "x": 14,
      "y": 0,
      "width": 5,
      "height": 6,
      "properties": {
          "metrics": [
              [ "${var.namespace}", "ppe-scan-timer.avg", "bucket", "kand2021imagebucket", { "region": "eu-west-1", "label": "Average time to scan for PPE violations", "color": "#ff9896" } ]
          ],
          "sparkline": false,
          "view": "singleValue",
          "region": "eu-west-1",
          "title": "Responstime to danger thresholds",
          "period": 300,
          "stat": "Average",
          "setPeriodToTimeRange": true,
          "trend": false,
          "stacked": true
      }
    },
    {
      "type": "metric",
      "x": 5,
      "y": 6,
      "width": 10,
      "height": 5,
      "properties": {
          "metrics": [
              [ "${var.namespace}", "sri-scan-timer.avg", "bucket ", "kand2021imagebucket", { "region": "eu-west-1", "id": "m2" } ]
          ],
          "view": "gauge",
          "region": "eu-west-1",
          "yAxis": {
              "left": {
                  "min": 0,
                  "max": 20000
              }
          },  
          "annotations": {
              "horizontal": [
                  [
                      {
                          "color": "#b2df8d",
                          "label": "Good",
                          "value": 5000
                      },
                      {
                          "value": 0,
                          "label": "Good"
                      }
                  ],
                  [
                      {
                          "color": "#dbdb8d",
                          "label": "Moderat",
                          "value": 12000
                      },
                      {
                          "value": 5000,
                          "label": "Moderat"
                      }
                  ],
                  {
                      "color": "#ff9896",
                      "label": "Danger!",
                      "value": 12000,
                      "fill": "above"
                  }
              ]
          },
          "period": 900,
          "stat": "Maximum",
          "title": "Time it takes to scan for helmets",
          "liveData": false,
          "singleValueFullPrecision": false,
          "legend": {
              "position": "hidden"
          },
          "setPeriodToTimeRange": true,
          "sparkline": false,
          "trend": false
      }
  }
  ]
}
SEC
}