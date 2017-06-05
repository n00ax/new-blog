(defproject new-blog "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"] 
                 [compojure "1.6.0"]
                 [http-kit "2.1.16"]
                 [selmer "1.10.7"]
                 [com.novemberain/monger "3.1.0"]
                 [com.ashafa/clutch "0.4.0"]
                 [org.slf4j/slf4j-nop "1.7.12"]
                 [ring/ring-defaults "0.3.0"]
                 [ring/ring-core "1.6.1"]
                 [org.clojure/data.json "0.2.6"]]
  :main ^:skip-aot new-blog.init
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
