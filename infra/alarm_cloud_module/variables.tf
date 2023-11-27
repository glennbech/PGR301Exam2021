variable "prefix" {
  description = "A prefix for naming resources"
  type        = string
}

variable "namespace" {
  description = "The namespace for the CloudWatch metric"
  type        = string
}

variable "metric_name" {
  description = "The name of the CloudWatch metric"
  type        = string
}

variable "threshold" {
  description = "The threshold to trigger the alarm"
  type        = number
}

variable "alarm_email" {
  description = "Email address to receive alarm notifications"
  type        = string
}
