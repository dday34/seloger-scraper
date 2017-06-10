(ns seloger-scraper.core
  (:require
   [clojure.string :as string]
   [net.cgrand.enlive-html :as html]
   [org.httpkit.client :as http])
  (:gen-class))

(def seloger-url "http://www.seloger.com/list.htm?tri=initial&idtypebien=2,1&pxmax=50000&idtt=2&ci=340172&naturebien=1,2,4")

(defn fetch-url [url]
  @(http/get url {:as :stream}))

(defn get-page []
  (fetch-url seloger-url))

(defn get-link [l]
  {:title (-> l html/text string/trim)
   :link (-> l :attrs :href)})

(defn get-links []
  (map get-link (html/select (html/html-resource (:body (get-page))) [:div.title :a])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
