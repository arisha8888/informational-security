
### 1) Выбрана для внедрения уязвимость Open redirect
### 2) Внедрение уязвимости:

Добавлен следующий метод в OrderController.java:

```java
@GetMapping("/orders/redirect")
    public RedirectView redirect(@RequestParam String url) {
        return new RedirectView(url, false);
    }
```

C:\Users>curl -v http://localhost:8080/orders/redirect?url=https://www.google.com
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
* using HTTP/1.x
> GET /orders/redirect?url=https://www.google.com HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.13.0
> Accept: */*
>
< HTTP/1.1 302
< Location: https://www.google.com
< Content-Language: en-US
< Content-Length: 0
< Date: Tue, 06 Oct 2026 13:25:09 GMT
<
* Connection #0 to host localhost left intact


### 3) fix уязвимости:

Добавлен следующий метод в OrderController.java:

```java
@GetMapping("/orders/safe-redirect")
    public RedirectView safeRedirect(@RequestParam String url) {
        // Разрешаем только относительные пути (начинаются с /)
        if (url.startsWith("/") && !url.startsWith("//")) {
            return new RedirectView(url, true);
        }
        // Если URL внешний или подозрительный — редиректим на главную страницу
        return new RedirectView("/", true);
    }
```

C:\Users>curl -v http://localhost:8080/orders/safe-redirect?url=https://www.google.com
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
* using HTTP/1.x
> GET /orders/safe-redirect?url=https://www.google.com HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.13.0
> Accept: */*
>
* Request completely sent off
< HTTP/1.1 302
< Location: http://localhost:8080/
< Content-Language: en-US
< Content-Length: 0
< Date: Tue, 06 Oct 2026 13:35:08 GMT
<
* Connection #0 to host localhost left intact
