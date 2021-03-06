(defproject seloger-scraper "0.1.0"
  :description "Scrape accomodation listings on seloger.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/tools.logging "0.4.0"]
                 [log4j/log4j "1.2.17"]
                 [org.slf4j/slf4j-log4j12 "1.7.12"]
                 [enlive "1.1.6"]
                 [http-kit "2.2.0"]
                 [sparkledriver "0.1.1"]]
  :main ^:skip-aot seloger-scraper.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
