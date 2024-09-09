package tis.techacademy.green_map;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@EnableCaching
@SpringBootApplication
public class GreenMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenMapApplication.class, args);
    }

    @Bean("CacheManager")
    @Primary
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

}
