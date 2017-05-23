(ns new-blog.backend.blog-backend
    (:gen-class)
    (:require [monger.core :as monger]
              [monger.collection]))

(defn grab-entry
    [id]
    "returns page map basec on id"
    (let [connection (monger/connect)
                 database (monger/get-db connection "blog-test")
                 coll "entries"]
         (first (monger.collection/find-maps database coll {:name id}))))
   
(defn grab-entry-d
    [id]
    "returns page map basec on id"
    (let [connection (monger/connect)
                 database (monger/get-db connection "blog-test")
                 coll "entries"]
         (first (monger.collection/find-maps database coll {:_id id}))))
   


