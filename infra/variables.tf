variable "aws_region" {
  description = "The AWS region to deploy resources in"
  type        = string
  default     = "eu-west-1"
}

variable "service_name" {
  description = "The name of the AWS App Runner service"
  type        = string
  default     = "edu-apprunner-service-kand-2021"
}

variable "ecr_registry" {
  description = "ECR registry URL"
  type        = string
  default     = "244530008913.dkr.ecr.eu-west-1.amazonaws.com"
}

variable "ecr_repository" {
  description = "ECR repository name"
  type        = string
  default     = "kand2021"
}

variable "iam_role_arn" {
  description = "IAM role ARN for App Runner"
  type        = string
  default     = "arn:aws:iam::244530008913:role/service-role/AppRunnerECRAccessRole"
}

variable "iam_role_name" {
  description = "Name of the IAM role for App Runner"
  type        = string
  default     = "kand2021-app-runner"
}