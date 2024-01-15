
# Run the Application on a Kubernetes Cluster using Helm

1. Set up a Kubernetes Cluster
   - Tested on: Kubernetes in Desktop Docker on Windows
     
2. Install the CLI Tools Kubectl & Helm
   - Kubectl: [Doku](https://kubernetes.io/docs/tasks/tools/)
   - Helm: [Doku](https://helm.sh/docs/intro/install/)
     
3. Install NGINX ingress controller for the cluster. (Command for [Docker Desktop on Windows](https://kubernetes.github.io/ingress-nginx/deploy/))
   ```
   kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.2/deploy/static/provider/cloud/deploy.yaml
   ```
   
4. Create folders for the local volumes in the repository root.
   ```
   ./mkdirs.sh
   ```
   
6. Customize the `values.yaml` configuration inside the `./helm` folder
   - *Important:* Value `localConfig.mountBasePath` must be set to the `volumes` directory created in previous step 4.
   - Everything else is preconfigured for the application to be ready for use. 

  

### Installing Application with Helm Chart

1. Install the Helm chart in root folder:
   ```
   helm install joboffers helm/.
   ```

3. Monitor the creation of the Kubernetes Pods in the cluster:
   ```
   kubectl get pods
   ```
   *Note:* When computer resources are low, Pods can get stuck at startup. Reinstalling the Helm chart can fix this problem.

5. Once all Pods are running, you will see the homepage at `http://localhost` (with the preconfigured values.yaml).
   
   *Note:* Even if the pod status is `Running`, the services may not yet be ready. It is recommended to wait ~5 min or to check the service status in the pod logs.

7. To clean up the Kubernetes application, uninstall the Helm chart.
   ```
   helm delete joboffers
   ```
