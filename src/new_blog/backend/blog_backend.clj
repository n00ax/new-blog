(ns new-blog.backend.blog-backend
    (:gen-class)
    (:require [monger.core :as mongo]
              [monger.collection :as mongo-collection]
              [monger.query :as mongo-query]
              [monger.operators :as mo]))

(def connect-uri "mongodb://server-node:12345@ds155411.mlab.com:55411/heroku_vsdsxqp7")
(def connect-database "heroku_vsdsxqp7")

(defn mongo-connect-shim
    []
    (if (= (System/getenv "IS_PRODUCTION") "true")
            (:conn (mongo/connect-via-uri connect-uri))
            (mongo/connect)))

(defn mongo-db-shim
    []
    (if (= (System/getenv "IS_PRODUCTION") "true")
        connect-database
        "clojure-blog"))

(defn grab-entry
    [id]
    "returns raw database entry as map via corrosponding id field"
    (let [connection (mongo-connect-shim)
          database (mongo/get-db connection (mongo-db-shim))]
        (mongo-collection/find-one-as-map database "entries" {:entry-title id})))

(defn grab-newest-entries
    [limit]
    "returns raw entries in ascending order created to limit as vector"
    (let [connection (mongo-connect-shim)
          database (mongo/get-db connection (mongo-db-shim))]
        (mongo-query/with-collection database "entries"
            (mongo-query/sort (array-map :_id -1))
            (mongo-query/limit limit))))

(defn grab-comments
    [id]
    (:entry-comments (grab-entry id)))
   
(defn write-comment
    [id date name comment]
    (println id date name comment)
    (let [connection (mongo-connect-shim)
            database (mongo/get-db connection (mongo-db-shim))]
        (mongo-collection/update database "entries" {:entry-title id} {mo/$push {:entry-comments{
                                                                                                 :comment-name name
                                                                                                 :comment-date date
                                                                                                 :comment-content comment}}} {:multi false})))
        
    
