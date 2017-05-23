(ns new-blog.config.init-config
    (:gen-class))
(defn server-configs 
    [debug-or-production]
    "takes key as to the desired config" 
    (debug-or-production 
    {
    :debug {
        :http-port 5050
    }
    :production{
        :http-port 4040}}))