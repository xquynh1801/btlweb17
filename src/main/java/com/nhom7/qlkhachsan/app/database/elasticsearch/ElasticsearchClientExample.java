package com.nhom7.qlkhachsan.app.database.elasticsearch;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

public class ElasticsearchClientExample {
    public static void main(String[] args) {
        // Khởi tạo và kết nối RestHighLevelClient tới Elasticsearch Server
        RestHighLevelClient elasticsearchClient = new RestHighLevelClient(
                RestClient.builder("localhost:9200")
        );

        try {
            // Thêm dữ liệu vào Elasticsearch
            IndexResponse response = addDataToElasticsearch(elasticsearchClient);
            System.out.println("Document ID: " + response.getId());

            // Tìm kiếm dữ liệu từ Elasticsearch
            searchElasticsearchData(elasticsearchClient, "example");

            // Đóng kết nối khi hoàn tất công việc
            elasticsearchClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức thêm dữ liệu vào Elasticsearch
    private static IndexResponse addDataToElasticsearch(RestHighLevelClient elasticsearchClient) throws IOException {
        String indexName = "my_index";
        String document = "{ \"name\": \"example\" }";

        IndexRequest request = new IndexRequest(indexName)
                .source(document, XContentType.JSON);

        return elasticsearchClient.index(request, RequestOptions.DEFAULT);
    }

    // Phương thức tìm kiếm dữ liệu từ Elasticsearch
    private static void searchElasticsearchData(RestHighLevelClient elasticsearchClient, String keyword) throws IOException {
        String indexName = "my_index";

        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("name", keyword));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("Search Results: " + searchResponse);
    }
}
