variable "PROJECT_ID" {
  description = "The GCP project ID"
  type        = string
}

variable "REGION" {
  description = "The GCP region"
  type        = string
}


variable "CREDENTIALS_FILE" {
  description = "The path to the GCP credentials file"
  type        = string
}

variable "INSTANCE_NAME" {
  description = "The GCE instance name"
  type        = string
}

variable "MACHINE_TYPE" {
  description = "The GCE machine type"
  type        = string
}

variable "NETWORK_NAME" {
  description = "The GCE network name"
  type        = string
}

variable "FIREWALL_NAME" {
  description = "The GCE firewall name"
  type        = string
}

variable "REPOSITORY_ID" {
  description = "The ID of the artifact repository"
  type        = string
}

variable "CLUSTER_NAME" {
  description = "The name of the GKE cluster"
  type        = string
}