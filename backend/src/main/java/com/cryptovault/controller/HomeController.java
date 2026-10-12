package com.cryptovault.controller;

import com.cryptovault.service.CurrencyService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    private final CurrencyService currencyService;

    public HomeController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tickers", currencyService.getMarketTickers());
        model.addAttribute("currencies", currencyService.getAllActiveCurrencies());
        model.addAttribute("pageTitle", "CryptoVault — Full-Stack Server-Rendered Wallet Platform");
        model.addAttribute("metaDescription", "CryptoVault is a high-performance, secure, server-rendered cryptocurrency wallet platform designed by Adabala Pushkaran (24EG105R65).");
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "About CryptoVault — Architecture & Security");
        model.addAttribute("metaDescription", "Learn about CryptoVault's 5-layer Spring Boot architecture, normalized MySQL schema, and device-first design.");
        return "about";
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        model.addAttribute("pageTitle", "Frequently Asked Questions — CryptoVault");
        model.addAttribute("metaDescription", "Common questions about CryptoVault security, transaction fees, multi-currency support, and testnet faucets.");
        return "faq";
    }

    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("pageTitle", "Terms of Service — CryptoVault");
        return "terms";
    }

    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("pageTitle", "Privacy Policy — CryptoVault");
        return "privacy";
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
                "  <url><loc>https://cryptovault.io/</loc><changefreq>daily</changefreq><priority>1.0</priority></url>\n" +
                "  <url><loc>https://cryptovault.io/about</loc><changefreq>weekly</changefreq><priority>0.8</priority></url>\n" +
                "  <url><loc>https://cryptovault.io/faq</loc><changefreq>weekly</changefreq><priority>0.8</priority></url>\n" +
                "  <url><loc>https://cryptovault.io/login</loc><changefreq>monthly</changefreq><priority>0.7</priority></url>\n" +
                "  <url><loc>https://cryptovault.io/register</loc><changefreq>monthly</changefreq><priority>0.9</priority></url>\n" +
                "</urlset>";
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String robots() {
        return "User-agent: *\n" +
                "Allow: /\n" +
                "Disallow: /dashboard\n" +
                "Disallow: /wallets\n" +
                "Disallow: /transactions\n" +
                "Disallow: /admin\n" +
                "Disallow: /profile\n" +
                "Sitemap: https://cryptovault.io/sitemap.xml\n";
    }
}
