variable "aws_region" {
  description = "AWS region where the EC2 instance will be created."
  type        = string
  default     = "ap-northeast-2"
}

variable "project_name" {
  description = "Name prefix for AWS resources."
  type        = string
  default     = "fruit-shop-back"
}

variable "instance_type" {
  description = "EC2 instance type."
  type        = string
  default     = "t3.small"
}

variable "key_pair_name" {
  description = "Existing AWS EC2 key pair name."
  type        = string
  default     = "kosta"
}

variable "ec2_user" {
  description = "Default SSH user for the selected AMI. Amazon Linux 2023 uses ec2-user."
  type        = string
  default     = "ec2-user"
}

variable "ssh_allowed_cidr_blocks" {
  description = "CIDR blocks allowed to connect by SSH. Replace with your public IP CIDR before production use."
  type        = list(string)
  default     = ["0.0.0.0/0"]
}

variable "app_allowed_cidr_blocks" {
  description = "CIDR blocks allowed to access the application port."
  type        = list(string)
  default     = ["0.0.0.0/0"]
}

variable "app_port" {
  description = "Application port exposed by docker-compose.ec2.yml."
  type        = number
  default     = 8080
}

variable "root_volume_size" {
  description = "Root EBS volume size in GiB."
  type        = number
  default     = 20
}
