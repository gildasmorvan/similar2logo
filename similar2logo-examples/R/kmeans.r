#datapp <- scale(datapp) #normalisation #mandatory if datapp require scaling

#Catégorisation de la base

k=5 #nombre de clusters
datappCluster <- kmeans(datapp, k, nstart = 20) #catégorisation de la base
#récupération des données originaux pour les centres des 10 clusters, ony applied if datapp
#is scaled
#datappCluster$centers = t(apply(datappCluster$centers,1,function(r)r*
                                  #attr(datapp,'scaled:scale') + 
                                  #attr(datapp, 'scaled:center')))

#le calcul inverse des centres entraine des valeurs réels à plus de 4 chiffres aprés 
#virgules, on arrondi pour obtenir les valeurs identiques
for (column in colnames(datapp)){
   datappCluster$centers[,column] = round(datappCluster$centers[,column],2)
}
observations_to_keep=vector()# initialisation du vecteur des observations à conserver
for(i in 1:k){ #l'operation se fait pour les 10 clusters
  cluster=datapp[which(datappCluster$cluster %in% i),] #cluster i
  if(NCOL(cluster)==1) {
  	observations_to_keep=c(observations_to_keep, #si le centre i du cluster i est une observation
                           which(apply(datapp,   #alors on conserve son indice
                                       1, 
                                       function(a)all(a==datappCluster$centers[i,])) == T)[[1]])
  } else if (duplicated(rbind(cluster, datappCluster$centers[i,]))[nrow(cluster)+1]){ 
    observations_to_keep=c(observations_to_keep, #si le centre i du cluster i est une observation
                           which(apply(datapp,   #alors on conserve son indice
                                       1, 
                                       function(a)all(a==datappCluster$centers[i,])) == T)[[1]])
  }else{
    distances=apply(cluster,  #sinon on calcul les distances des observations du cluster i
                    1,        #par rapport au centre i et on les affecte au vector distances
                    function(a)all(as.numeric(dist(rbind(datappCluster$centers[i,],a)))))
    observations_to_keep=c(observations_to_keep, #par suite on cherche l'indice de l'observation
                           which(apply(datapp,  #qui correspond à la distance minimale par
                                       1,    # rapport au centre i et on le met dans notre vecteur
                                       function(a)all(a==cluster[which(distances == min(distances))[[1]],])) == T)[[1]])
  }
}
# le vecteur observations.to.keep avec 10 indices d'observations à conserver (pour pas de temps 1000) 
observations_to_keep