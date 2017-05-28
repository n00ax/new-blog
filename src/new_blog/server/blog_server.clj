(ns new-blog.server.blog-server
    (:gen-class)
    (:use [compojure.core])
    (:require [compojure.core] [compojure.route] 
        [org.httpkit.server] 
        [selmer.parser :as renderer]
        [new-blog.backend.blog-backend :as backend]
        [ring.middleware.defaults]
        [ring.util.response :as ring.response]))

(defn get-page-path 
    [path] 
    (str "resources/static/pages/" path))

(defn render-index
    [path]
    (renderer/render (slurp (get-page-path path))
        (let [top-data (backend/grab-newest-entries 3)
                entry-one (first top-data)
                entry-two (second top-data)
                entry-three (nth top-data 2)
                limit-entry-content-size (fn [entry size]
                                            (assoc entry :entry-content
                                                (if (> (count (:entry-content entry)) size) 
                                                    (str (subs (:entry-content entry) 0 size) "...")
                                                    (:entry-content entry))))]
            {:new-post-one (limit-entry-content-size entry-one 220)
                :new-post-two (limit-entry-content-size entry-two 220)
                :new-post-three (limit-entry-content-size entry-three 220)
                :first-post-url (str "entries/" (:entry-title entry-one) "/post")
                :second-post-url (str "entries/" (:entry-title entry-two) "/post")
                :third-post-url (str "entries/" (:entry-title entry-three) "/post")})))

(defn render-post
    [path query]
    (renderer/render (slurp (get-page-path path))
        (let [page-data (backend/grab-entry query)
                comment-data (backend/grab-comments query)]
            (if (= page-data nil)
                {:entry-title "Entry Not Found.."}
                {:entry-title (:entry-title page-data)
                    :entry-date (:entry-date page-data)
                    :entry-content (:entry-content page-data)
                    :entry-comments comment-data}))))

(defn middle [post-name post-comment entryid]
    (backend/write-comment entryid "June 1st" post-name post-comment)
    (ring.response/redirect "post"))

(compojure.core/defroutes primary-routes
    (compojure.core/GET "/" [] (render-index "index.html"))
    (compojure.core/context "/entries/:entryid" [entryid] 
        (compojure.core/GET "/post" [] (render-post "post.html" entryid))
        (compojure.core/GET "/comment" request
            (let [post-name (:person-name (:params request))
                    post-comment (:person-comment (:params request))]
                (backend/write-comment entryid "June 1st" post-name post-comment)
                (ring.response/redirect "post"))))
    (compojure.route/resources "/" {:root "static"})
    (compojure.route/not-found (renderer/render (slurp (get-page-path "404.html")) {})))

(defn start-blog
    "Starts server from destroot, takes init parameter block [init_config]"
    [init-config]
    (renderer/cache-off!)
    (org.httpkit.server/run-server (ring.middleware.defaults/wrap-defaults primary-routes ring.middleware.defaults/site-defaults) 
        (if (= (System/getenv "PORT") nil)
            {:port (:http-port init-config)}
            {:port (read-string (System/getenv "PORT"))})))
