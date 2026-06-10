output "instance_id" {
  description = "Created EC2 instance ID."
  value       = aws_instance.app.id
}

output "public_ip" {
  description = "Public IP address for GitHub Actions EC2_HOST secret."
  value       = aws_instance.app.public_ip
}

output "public_dns" {
  description = "Public DNS name of the EC2 instance."
  value       = aws_instance.app.public_dns
}

output "ssh_user" {
  description = "SSH username for GitHub Actions EC2_USER secret."
  value       = var.ec2_user
}

output "ssh_command" {
  description = "Example SSH command."
  value       = "ssh -i <path-to-kosta-private-key> ${var.ec2_user}@${aws_instance.app.public_ip}"
}
