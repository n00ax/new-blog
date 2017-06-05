(ns new-blog.backend.blog-backend
    (:gen-class)
    (:require [monger.core :as mongo]
              [monger.collection :as mongo-collection]
              [monger.query :as mongo-query]
              [monger.operators :as mo]))

(defn connect-uri 
    []
    (System/getenv "MONGODB_URI"))
(defn connect-database
    [] 
    (System/getenv "MONGODB_DATABASE"))

(defn mongo-connect-shim
    []
    (if (= (System/getenv "IS_PRODUCTION") "true")
        (:conn (mongo/connect-via-uri (connect-uri)))
        (mongo/connect)))

(defn mongo-db-shim
    []
    (if (= (System/getenv "IS_PRODUCTION") "true")
        (connect-database)
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
    "returns comments based on entry id (aka title) and returns as vector of maps"
    (:entry-comments (grab-entry id)))
   
(defn write-comment
    [id date name comment]
    "writes comment to article [id] with date (string) name (string) and comment (string)"
    (println id date name comment)
    (let [connection (mongo-connect-shim)
            database (mongo/get-db connection (mongo-db-shim))]
        (mongo-collection/update database "entries" {:entry-title id} {mo/$push {:entry-comments{
                                                                                                 :comment-name name
                                                                                                 :comment-date date
                                                                                                 :comment-content comment}}} {:multi false})))
        
(defn search-comment
    [keyword]
    (let [connection (mongo-connect-shim)
            database (mongo/get-db connection (mongo-db-shim))]    
        (mongo-collection/find-maps database "entries" {:entry-title {mo/$regex (str keyword) mo/$options "i"}})))

(defn convert-keys-to-csv
    [key entries]
    (reduce #(str %1 "," %2) (map key entries)))
