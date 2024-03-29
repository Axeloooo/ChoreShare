variable "REGION" {
  description = "The GCP region"
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

variable "IMAGE_FAMILY" {
  description = "The GCE image family"
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
