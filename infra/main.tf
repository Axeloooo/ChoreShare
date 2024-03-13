module "gce" {
  source = "./modules/gce"

  INSTANCE_NAME = var.INSTANCE_NAME
  MACHINE_TYPE  = var.MACHINE_TYPE
  REGION        = var.REGION
  NETWORK_NAME  = var.NETWORK_NAME
  FIREWALL_NAME = var.FIREWALL_NAME
}

module "gar" {
  source = "./modules/gar"

  REGION        = var.REGION
  REPOSITORY_ID = var.REPOSITORY_ID
}

module "gke" {
  source = "./modules/gke"

  REGION       = var.REGION
  CLUSTER_NAME = var.CLUSTER_NAME
}
