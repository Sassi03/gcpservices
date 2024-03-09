# gcpservices
Spring boot application to extract document using google document AI

Install Google Cloud SDK:

Download and install the Google Cloud SDK for your operating system.

Open a terminal or command prompt and run:
gcloud init
Follow the prompts to log in to your Google Cloud account, select your project, and configure the default settings.

After initializing the SDK, you can authenticate with Application Default Credentials using:
gcloud auth application-default login

Set the default project ID for your application:
gcloud config set project YOUR_PROJECT_ID
Replace YOUR_PROJECT_ID with your actual Google Cloud project ID.

Verify your current configuration settings:
gcloud config list
