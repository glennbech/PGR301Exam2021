resource "aws_apprunner_service" "service" {
  service_name = var.service_name 

  instance_configuration {
    instance_role_arn = aws_iam_role.role_for_apprunner_service.arn
    cpu               = 256  
    memory            = 1024
  }

  source_configuration {
    authentication_configuration {
      access_role_arn = var.iam_role_arn
    }
    image_repository {
      image_configuration {
        port = "8080"
      }
      image_identifier      = "${var.ecr_registry}/${var.ecr_repository}:latest"
      image_repository_type = "ECR"
    }
    auto_deployments_enabled = true
  }
}

resource "aws_iam_role" "role_for_apprunner_service" {
  name               = var.iam_role_name
  assume_role_policy = data.aws_iam_policy_document.assume_role.json
}

data "aws_iam_policy_document" "assume_role" {
  statement {
    effect = "Allow"

    principals {
      type        = "Service"
      identifiers = ["tasks.apprunner.amazonaws.com"]
    }

    actions = ["sts:AssumeRole"]
  }
}

data "aws_iam_policy_document" "policy" {
  statement {
    effect    = "Allow"
    actions   = ["rekognition:*"]
    resources = ["*"]
  }
  
  statement  {
    effect    = "Allow"
    actions   = ["s3:*"]
    resources = ["*"]
  }

  statement  {
    effect    = "Allow"
    actions   = ["cloudwatch:*"]
    resources = ["*"]
  }
}

resource "aws_iam_policy" "policy" {
  name        = "aws_apr_policy"
  description = "Premission Policy for apprunner instance"
  policy      = data.aws_iam_policy_document.policy.json
}


resource "aws_iam_role_policy_attachment" "attachment" {
  role       = aws_iam_role.role_for_apprunner_service.name
  policy_arn = aws_iam_policy.policy.arn
}

module "cloudwatch_alarm" {
  source       = "./alarm_cloud_module"
  prefix       = "RekognitionController"
  namespace    = "2021ConstructionSecurityCo"
  metric_name  = "tank-detected-metric.count"
  threshold    = 1
  alarm_email  = "martinsorensen96@gmail.com"
}