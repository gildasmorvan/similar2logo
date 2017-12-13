# kmeans on the simulation data, preference to min number of preys
data.kmeans <- kmeans(datapp, k)

# Initializing the vector 
observations_to_keep=vector()

# for loop on all k cluster
for(i in 1:k){
  cluster=datapp[which(data.kmeans$cluster %in% i),] #cluster i

  # If cluster i is a singleton
  # Mystery: why not NROW?
  if(NCOL(cluster)==1) {
    print("singleton")
    observations_to_keep=c(observations_to_keep, 
                           which(apply(datapp,   
                           1, 
                           function(a)all(a==data.kmeans$centers[i,])) == T)[[1]])
  }else{
  cluster_min <- apply(cluster, 2, min)[4]
  observations_to_keep <- c(observations_to_keep, 
                            which(datapp[,4,drop=F]==cluster_min)[1])}
}
observations_to_keep