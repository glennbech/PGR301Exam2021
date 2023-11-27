resource "aws_cloudwatch_metric_alarm" "tank_detection_alarm" {
  alarm_name          = "${var.prefix}-tank-detection"
  namespace           = var.namespace
  metric_name         = var.metric_name

  comparison_operator = "GreaterThanThreshold"
  threshold           = var.threshold
  evaluation_periods  = "5"
  datapoints_to_alarm = 1
  period              = "10"
  statistic           = "Sum"
  treat_missing_data = "notBreaching"

  alarm_description   = "Alarm when a tank is detected."
  alarm_actions       = [aws_sns_topic.tank_alert.arn]
  
  dimensions = {
    resource = "image"
  }
}

resource "aws_sns_topic" "tank_alert" {
  name = "${var.prefix}-tank-alert-topic"
}

resource "aws_sns_topic_subscription" "tank_alert_email" {
  topic_arn = aws_sns_topic.tank_alert.arn
  protocol  = "email"
  endpoint  = var.alarm_email
}
