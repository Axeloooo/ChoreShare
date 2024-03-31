output "choreshare_registry_name" {
  value = module.gar.choreshare_registry_name
  description = "The name of the GCR repository"
}

output "choreshare_registry_id" {
  value = module.gar.choreshare_registry_id
  description = "The URL of the GCR repository"
}

output "choreshare_registry_location" {
  value = module.gar.choreshare_registry_location
  description = "The location of the GCR repository"
}

output "choreshare_vm_name" {
  value = module.gce.choreshare_vm_name
  description = "The name of the GCE instance"
}

output "choreshare_vm_external_ip" {
  value       = module.gce.choreshare_vm_external_ip
  description = "The external IP address of the GCE instance"
}

output "choreshare_cluster_name" {
  value = module.gke.choreshare_cluster_name
  description = "The name of the GKE cluster"
}