terraform {
  backend "s3" {
    bucket         = "pgr301-2021-terraform-state"
    key            = "kand2021/state/apprunner.state"
    region         = "eu-north-1"
    encrypt        = true
  }
}