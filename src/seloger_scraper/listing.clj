(ns seloger-scraper.listing
  (:require
   [clojure.data.json :as json]
   [sparkledriver.core :refer [fetch! find-by-id text]]))

(defn scrape [browser output-file-name url]
  (println "scrape listing " url)
  (let [listing-page (fetch! browser url)
        listing {:title (-> listing-page (find-by-id "js-titleAnnonce") text)
                 :description (-> listing-page (find-by-id "js-descriptifBien") text)}]
    (spit output-file-name (json/write-str listing) :append true)
    listing))


