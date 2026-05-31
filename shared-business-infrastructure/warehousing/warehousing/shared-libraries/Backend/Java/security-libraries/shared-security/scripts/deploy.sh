#!/bin/bash
echo "Deploying $module configuration..."
kubectl apply -f ../k8s/
