
# Run the Application within Kubernetes

 ## Running on Windows:

### Prerequisites:

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

  

### Installing Application with Helm Chart

1. Go to root folder and type command `helm install joboffers helm/.`

2. Now type in `kubectl get pods` and you should see the list of pods being created. If pods are stuck at pending, it is due to low resources on the host computer. Deleting the deployment with ´helm delete joboffers´ and reinstalling often fixes the problem.

3. After all pods have started, go to `http://localhost`. You should now see the homepage

4. Cleanup can be done with `helm delete joboffers`

## Running on Linux in VM

### Prerequisites:
1. Operating System
- Tested on: Debian 12.2.0 "Bookworm" ([AMD 64 bit, netinst](https://cdimage.debian.org/debian-cd/current/amd64/iso-cd/)) inside VirtualBox VM
2. Install curl: `sudo  apt  install  curl`
3. Install Docker ([Taken from official documentation](https://docs.docker.com/engine/install/debian/)): 
```
# Add Docker's official GPG key:

sudo  apt-get  update
sudo  apt-get  install  ca-certificates  curl  gnupg
sudo  install  -m  0755  -d  /etc/apt/keyrings
curl  -fsSL  https://download.docker.com/linux/debian/gpg  |  sudo  gpg  --dearmor  -o  /etc/apt/keyrings/docker.gpg
sudo  chmod  a+r  /etc/apt/keyrings/docker.gpg

# Add the repository to Apt sources:

echo  \
"deb [arch=$(dpkg  --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian \
$(. /etc/os-release && echo "$VERSION_CODENAME") stable"  |  \
sudo  tee  /etc/apt/sources.list.d/docker.list  >  /dev/null

sudo  apt-get  update

# Install docker
sudo  apt-get  install  docker-ce  docker-ce-cli  containerd.io  docker-buildx-plugin  docker-compose-plugin

# Add user to docker group
sudo  usermod  -aG  docker  $USER && newgrp  docker
```
4. Install Minikube ([Taken from official documentation](https://kubernetes.io/de/docs/tasks/tools/install-minikube/#linux)): 
```
# Download minikube
curl  -Lo  minikube  https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64 && chmod  +x  minikube

# Add minikube to path
sudo  cp  minikube  /usr/local/bin && rm  minikube
```
5. Install kubectl: ([Taken from official documentation](https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/))
```
curl  -LO  "https://dl.k8s.io/release/$(curl  -L  -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo  install  -o  root  -g  root  -m  0755  kubectl /usr/local/bin/kubectl
rm  kubectl
```
6. Install helm: ([Taken from official documentation](https://helm.sh/docs/intro/install/#from-script))
```
curl  -fsSL  -o  get_helm.sh  https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
chmod  700  get_helm.sh
./get_helm.sh
rm  get_helm.sh
```

### Start the deployment
1. Start minikube: `minikube  start  --memory  8192  --cpus  4` (Adjust the resources as you see fit. This is the recommended setting)
2. Enable ingress for minikube: `minikube  addons  enable  ingress`
3. Create volume dirs in the root of the git repository and change permissions: `sh  mkdirs.sh && chmod -R ugo+wrx ./volumes`
4. Mount the created volume dirs inside the k8s node: `minikube  mount  $(pwd)/volumes:/data/volumes`
5. Bind the ingress ip to localhost: `minikube  tunnel  --bind-address='localhost'`
6. Create the deploymen: `helm install joboffers ./helm`