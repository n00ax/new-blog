(ns new-blog.init
  (:gen-class)
  (:require [new-blog.config.init-config] [new-blog.server.blog-server]))

(defn -main
  "Init Entry Point"
  [& args]
  (println "NEW")
  (if (= (System/getenv "IS_HEROKU") "true")
    (new-blog.server.blog-server/start-blog (do (println "HEROKU")(new-blog.config.init-config/server-configs :production)))
    (if args
      (let [init-config (new-blog.config.init-config/server-configs (read-string (first args)))]
        (println "New-Blog, N00AX's Blog Rewritten In Clojure\n[Mode Selected"
          (first args)"]")
        (new-blog.server.blog-server/start-blog init-config))
      (println "Usage: application {:profile} - (:production or :debug)"))))

