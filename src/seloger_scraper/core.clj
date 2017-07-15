(ns seloger-scraper.core
  (:require
   [clojure.string :as string]
   [net.cgrand.enlive-html :as html]
   [org.httpkit.client :as http]
   [seloger-scraper.listing :as listing])
  (:gen-class))

(defn fetch-url [url]
  @(http/get url {:as :stream}))

(defn get-link [l]
  (-> l :attrs :href))

(defn get-listing-links [url]
  (map get-link (html/select (html/html-resource (:body (fetch-url url))) [:div.title :a])))

(defn scrape-seloger-listings [url]
  (let [listing-links (get-listing-links url)]
    (println "Start scraping " (count listing-links) " listings")
    (map listing/scrape-listing listing-links)))

(defn get-city-id [city]
  (let [city-ids {"montpellier" "340172"}]
      (get city-ids city)))

(defn search [city max-price]
  (scrape-seloger-listings (str "http://www.seloger.com/list.htm?tri=initial&idtypebien=2,1&idtt=2&ci=" (get-city-id city) "&pxmax=" max-price "&naturebien=1,2,4")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
