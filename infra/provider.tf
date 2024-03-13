provider "google" {
  credentials = file(var.CREDENTIALS_FILE)
  project     = var.PROJECT_ID
  region      = var.REGION
}
