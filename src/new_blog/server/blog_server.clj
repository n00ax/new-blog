(ns new-blog.server.blog-server
    (:gen-class)
    (:require [compojure.core] [compojure.route] 
        [org.httpkit.server] 
        [clostache.parser :as renderer]
        [new-blog.backend.blog-backend :as backend]))

(defn get-page-path 
    [path] 
    (str "resources/static/pages/" path))

(defn render-index
    [path]
    (renderer/render (slurp (get-page-path path)) 
        {:date "1/2/3"}))

(defn render-post
    [path query]
    (renderer/render (slurp (get-page-path path))
                    (let [page-data (backend/grab-entry query)]
                        {:entry-title (:name page-data)
                         :entry-date (:date-created page-data)})))

(compojure.core/defroutes primary-routes
    (compojure.core/GET "/" [] (render-index "index.html"))
    (compojure.core/GET "/entries" [entry-id] (render-post "post.html" entry-id))
    (compojure.route/resources "/" {:root "static"})
    (compojure.route/not-found (renderer/render (slurp (get-page-path "404.html")))))

(defn start-blog
    "Starts server from destroot, takes init parameter block [init_config]"
    [init-config]
    (org.httpkit.server/run-server primary-routes {:port ((System/getenv "PORT"))}))