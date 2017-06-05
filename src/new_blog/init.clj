(ns new-blog.init
  (:gen-class)
  (:require [new-blog.config.init-config] [new-blog.server.blog-server]))

(defn -main
  "Init Entry Point"
  [& args]
  (if (= (System/getenv "IS_PRODUCTION") "true")
    (new-blog.server.blog-server/start-blog 
      (do (println "New-Blog, Heroku Enviornment Detected, Bypassing---> Hosted Mdoe")
          (new-blog.config.init-config/server-configs :production)))
    (if args
      (let [init-config (new-blog.config.init-config/server-configs (read-string (first args)))]
        (println "New-Blog, N00AX's Blog Rewritten In Clojure--> Nonhosted Mode\n[Mode Selected"
          (first args)"]")
        (new-blog.server.blog-server/start-blog init-config))
      (println "New-Blog 1.0, No ENV! \nUsage: application {:profile} - (:production or :debug)"))))

