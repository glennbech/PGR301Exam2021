output "alarm_arn" {
  description = "The ARN of the CloudWatch Alarm"
  value       = aws_cloudwatch_metric_alarm.tank_detection_alarm.arn
}

output "sns_topic_arn" {
  description = "The ARN of the SNS Topic"
  value       = aws_sns_topic.tank_alert.arn
}