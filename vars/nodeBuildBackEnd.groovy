#!/usr/bin/env groovy
void call() {
    String name = "backend"
    String buildFolder = "backend"
    String baseImage     = "node"
    String baseTag       = "lts-buster"
    String demoRegistry = "663535708029.dkr.ecr.ap-south-1.amazonaws.com"
    String awsRegion = "ap-south-1"
    String ecrRegistryUrl = "https://663535708029.dkr.ecr.ap-south-1.amazonaws.com"
    String awsCredential = 'aws-credentials'
    String ecrCredential = 'ecr-credentials'
    String k8sCredential = 'ekstest'
    String namespace = "demo"

//========================================================================
//========================================================================

//========================================================================
//========================================================================

    stage ('Prepare Package') {
        script {
            writeFile file: '.ci/Dockerfile', text: libraryResource('node/Dockerfile')
        }
    }

    stage ("Build Solution") {
        docker.build("ecr-dungnguyen-devops-${name}:${BUILD_NUMBER}", " -f ./.ci/Dockerfile \
        --build-arg BASEIMG=${baseImage} --build-arg IMG_VERSION=${baseTag} ${WORKSPACE}/src/${buildFolder}") 
    }

    stage ("Push Docker Images") {
        echo "Push Docker Images"
        
        docker.withRegistry(ecrRegistryUrl, "ecr:${awsRegion}:${awsCredential}") {
            sh "docker tag ecr-dungnguyen-devops-${name}:${BUILD_NUMBER} ${demoRegistry}/ecr-dungnguyen-devops-${name}:${BUILD_NUMBER}"
            sh "docker push ${demoRegistry}/ecr-dungnguyen-devops-${name}:${BUILD_NUMBER}"

            sh "docker tag ${demoRegistry}/ecr-dungnguyen-devops-${name}:${BUILD_NUMBER} ${demoRegistry}/ecr-dungnguyen-devops-${name}:latest"
            sh "docker push ${demoRegistry}/ecr-dungnguyen-devops-${name}:latest"
        }
    }
}

//========================================================================
// node CI
// Version: v1.0
// Updated:
//========================================================================
//========================================================================
// Notes:
//
//
//========================================================================