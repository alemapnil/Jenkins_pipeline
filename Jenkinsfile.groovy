// 以下是在名為wsl_node_1的node上，建置pipeline的Jenkinsfile

pipeline{
    agent {
        label 'wsl_node_1'
    }
    stages{
        stage('init'){
            steps{
                echo "此為Git上的Jenkinsfile"
                git branch: 'main', credentialsId: '7c359d4c-fd1b-49df-97df-7867f3e19e0f', url: 'https://github.com/alemapnil/Jenkins_pipeline.git'
                sh 'dos2unix *.sh'
                sh 'chmod +x *.sh'
                sh 'pwd'
                sh 'ls -al'
            }
        }
        
        stage('Build'){
            steps{
                sh "./Build.sh"
                echo "Build 成功了"
            }
        }
        stage('Unit'){
            steps{
                sh "./Unit.sh"
                echo "Unit 通過成功"
            }
        }
        stage('Quality'){
            steps{
                sh "./Quality.sh"
                echo "Quality通過成功"
                // error "發生錯誤"
            }
        }
        stage('Deploy'){
            steps{
                sh './Deploy.sh'
                echo "Deploy成功"
            }
        }
    }
    post{
        always{
            echo"總是執行"
        }
        success{
            echo"成功才執行"
                mail to: 'pamela.lin@nadisystem.com',
                subject: "[Pipeline] ${currentBuild.fullDisplayName} - ${currentBuild.currentResult}",
                body: "請查看 Jenkins Console: ${env.BUILD_URL}"
        }
        failure{
            echo"失敗才執行"
                mail to: 'pamela.lin@nadisystem.com',
                subject: "[Pipeline] ${currentBuild.fullDisplayName} - ${currentBuild.currentResult}",
                body: "請查看 Jenkins Console: ${env.BUILD_URL}"
        }
        unstable{
            echo"不穩定才執行"
        }
        changed{
            echo"狀態改變才執行，例如先前失敗或成功，反之亦然"
        }
    }
}