# Run the Application within Kubernetes

## Prerequisites:
1. Running Kubernetes Cluster
	- Tested on: Kubernetes in Desktop Docker on Windows
2. CLI Tools installed: Kubectl & Helm
	- Kubectl: [Doku](https://kubernetes.io/docs/tasks/tools/)
	- Helm: [Doku](https://helm.sh/docs/intro/install/)
3. Install ingress controller in kubernetes cluster
	- Example: [Doku](https://kubernetes.github.io/ingress-nginx/deploy/)
	- Command tested: `kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.2/deploy/static/provider/cloud/deploy.yaml`
4. Execute `mkdirs.sh` to create the `volumes` folder.
5. Adjust the base configuration under the `./helm` folder
    - Everything should be already configured. Only value that should be adjusted is `localConfig.mountBasePath`, which is the path to the `volumes` folder created in the previous step

## Installing Application with Helm Chart
1. Go to root folder and type command `helm install joboffers helm/.`
2. Now type in `kubectl get pods` and you should see the list of pods being created. If pods are stuck at pending, it is due to low resources on the host computer. Deleting the deployment with ´helm delete joboffers´ and reinstalling often fixes the problem.
3. After all pods have started, go to `http://localhost`. You should now see the homepage
4. Cleanup can be done with `helm delete joboffers`
