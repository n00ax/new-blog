(defproject new-blog "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"] 
                 [compojure "1.1.8"]
                 [http-kit "2.1.16"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [com.novemberain/monger "3.1.0"]
                 [com.ashafa/clutch "0.4.0"]]
  :main ^:skip-aot new-blog.init
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
