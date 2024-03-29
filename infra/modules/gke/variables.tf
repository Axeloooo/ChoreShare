variable "REGION" {
  description = "The region in which to create the resources"
  type        = string
}

variable "CLUSTER_NAME" {
  description = "The name of the GKE cluster"
  type        = string
}

variable "NODE_POOL_NAME" {
  description = "The name of the node pool"
  type        = string
}

variable "MACHINE_TYPE" {
  description = "The machine type of the node pool"
  type        = string
}

variable "SERVICE_ACCOUNT_EMAIL" {
  description = "The email of the service account"
  type        = string
}