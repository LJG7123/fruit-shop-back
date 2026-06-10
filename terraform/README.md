# Terraform EC2 Deployment

이 디렉터리는 `fruit-shop-back` 배포용 EC2 인스턴스를 생성합니다.
기존 AWS 키페어 `kosta`를 사용하며, Amazon Linux 2023 기반 EC2에 Docker와 Docker Compose를 설치합니다.

## 생성 리소스

- EC2 instance
- Security group
- 기본 VPC의 subnet 사용
- Docker / Docker Compose 설치 user-data

## 사용 방법

```bash
cd terraform
terraform init
terraform plan
terraform apply
```

## 주요 변수

기본 리전은 서울 리전(`ap-northeast-2`)입니다.

```bash
terraform apply \
  -var='ssh_allowed_cidr_blocks=["YOUR_PUBLIC_IP/32"]'
```

`ssh_allowed_cidr_blocks` 기본값은 실습 편의를 위해 `0.0.0.0/0`입니다.
실제 운영에서는 반드시 본인 IP의 `/32` CIDR로 제한하세요.

## GitHub Actions Secrets 설정

`terraform apply` 후 출력값을 GitHub Actions secrets에 등록하세요.

- `EC2_HOST`: `public_ip` 출력값
- `EC2_USER`: `ssh_user` 출력값, 기본값 `ec2-user`
- `EC2_SSH_KEY`: 기존 `kosta` 키페어의 private key 내용

애플리케이션 배포에는 기존 워크플로우의 `MYSQL_URL`, `MYSQL_USER`, `MYSQL_PASSWORD`, `DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`도 필요합니다.
