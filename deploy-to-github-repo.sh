git clone git@github.com:keyz182/maven-repo.git /tmp/mvnrepo
mvn -DaltDeploymentRepository=snapshot-repo::default::file:/tmp/mvnrepo/snapshots clean deploy
cd /tmp/mvnrepo
git commit -a --msg "Maven commit"
git push
