package com.company.working;

import com.company.ApiCall.CveApiCall;
import com.company.ApiModels.CVEModel;
import com.company.ApiModels.User;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Getter
@Setter
public class JsoupSiteParser
{
    Logger log = Logger.getLogger(JsoupSiteParser.class.getName());
    
    private boolean znacznik = false;
    private String url;
    private Set<String> stringSet;
    private List<String> mediaList;
    private List<String> linkList;
    private List<String> importsList;
    private List<String> metaList;
    private List<CVEModel> cveModelList;
    private List<User> usersNamesListFromAuthorApi;
    private String wordpress;
    private List<String> adminUsernames;
    private String cmsType;
    
    public JsoupSiteParser(String url)
    {
        this.stringSet = new HashSet<>();
        this.mediaList = new ArrayList<>();
        this.linkList = new ArrayList<>();
        this.importsList = new ArrayList<>();
        this.metaList = new ArrayList<>();
        this.cveModelList = new ArrayList<>();
        this.adminUsernames = new ArrayList<>();
        
        this.url = url;
        try
        {
            Scann();
            if(isZnacznik())
            {
                wpJson(getStringSet());
                getUsersByAuthor(url);
                getAdmin(getStringSet());
                getUsersByAuthor(url);
            }
            else
            {
                cmsType = " nie korzysta z WordPress";
            }
        }
        catch(IOException e)
        {
            log.info(e.getMessage());
        }
    }
    
    private void Scann() throws IOException
    {
    
        Document doc = Jsoup.connect("http://" + url)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .timeout(5000).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        Elements meta = doc.getElementsByTag("meta");
        try
        {
            mediaParser(media);
            importParser(imports);
            linksParser(links);
            metaParser(meta);
            
        }
        catch(Exception e)
        {
            log.info("Exception " + e);
        }
        
    }
    
    private void mediaParser(Elements media)
    {
        for(Element src : media)
        {
            if(src.tagName().equals("img"))
            {
                mediaList.add(src.attr("abs:src"));
            }
            if(src.attr("abs:src").contains("wp-content"))
            {
                znacznik = true;
            }
        }
    }
    
    private void importParser(Elements imports)
    {
        for(Element link : imports)
        {
            importsList.add(link.attr("abs:href"));
            
            String wpJson = link.attr("abs:href");
            if(wpJson.toLowerCase().contains("wp-json"))
            {
                String[] edit = link.attr("abs:href").split("wp-json" + 1);
                stringSet.add(edit[0]);
                znacznik = true;
            }
            
        }
    }
    
    private void linksParser(Elements links)
    {
        for(Element link : links)
        {
            linkList.add(link.attr("abs:href"));
        }
    }
    
    private void metaParser(Elements meta)
    {
        for(Element metas : meta)
        {
            metaList.add(String.valueOf(metas.attr("name", "generator")));
            wordpress = meta.attr("content");
            if(wordpress.contains("WordPress"))
            {
                znacznik = true;
            }
            
        }
    }
    
    public List<String> wpJson(Set<String> element)
    {
        List<String> strings = new ArrayList<>();
        Iterator<String> stringIterator;
        String url;
        URL obj;
        
        try
        {
            stringIterator = element.iterator();
            url = stringIterator.next();
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
            if(element.size() != 0)
            {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
                
                String inputLine;
                StringBuffer response = new StringBuffer();
                while((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                    
                }
                in.close();
                JSONObject myresponse = new JSONObject(response.toString());
                JSONArray rates_object = new JSONArray(myresponse.getJSONArray("namespaces").toString());
                CveApiCall cveApiCall = new CveApiCall();
                
                if(rates_object != null)
                {
                    
                    for(int i = 0; i < rates_object.length(); i++)
                    {
                        strings.add(rates_object.getString(i));
                    }
                }
                for(String s : strings)
                {
                    cveApiCall.read(s.split("/")[0], 1);
                    cveModelList = cveApiCall.getCveModelList();
                }
                
            }
        }
        catch(Exception e)
        {
            log.info("Exception " + e);
        }
        
        return strings;
        
    }
    
    public List<String> getAdmin(Set<String> element)
    {
        adminUsernames = new ArrayList<>();
        Iterator<String> stringIterator;
        String url;
        URL obj;
        
        try
        {
            stringIterator = element.iterator();
            url = stringIterator.next();
            obj = new URL(url + "wp/v2/users");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
            if(element.size() != 0)
            {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
                
                String inputLine;
                String read = "";
                while((inputLine = in.readLine()) != null)
                {
                    read = inputLine;
                }
                in.close();
                JSONArray myresponse = new JSONArray(read);
                for(int i = 0; i < myresponse.length(); i++)
                {
                    JSONObject jsonObject1 = myresponse.getJSONObject(i);
                    String name = jsonObject1.optString("name");
                    adminUsernames.add(name);
                }
                
            }
        }
        catch(Exception e)
        {
            log.info("Exception " + e);
        }
        return adminUsernames;
    }
    
    public List<User> getUsersByAuthor(String url)
    {
        usersNamesListFromAuthorApi = new ArrayList<>();
        URL obj;
        HttpURLConnection con;
        Elements links;
        Document doc;
        try
        {
            int i = 1;
            obj = new URL("http://" + url);
            con = (HttpURLConnection)obj.openConnection();
            String registeredNames;
            while(con.getResponseCode() != 404)
            {
                obj = new URL("http://" + url + "/?author=" + i);
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
                doc = Jsoup.connect(obj.toString()).get();
                registeredNames = doc.getElementsByTag("title").text().split("–")[0];
                links = doc.getElementsByAttribute("href");
                String linkWithUsername;
                for(Element e : links)
                {
                    linkWithUsername = e.attr("abs:href");
                    if(linkWithUsername.contains("feed"))
                    {
                        User user = new User(i, linkWithUsername.substring(linkWithUsername.lastIndexOf("author/") + 7).split("/")[0], registeredNames);
                        if(!user.getUserName().isEmpty())
                        {
                            usersNamesListFromAuthorApi.add(user);
                        }
                    }
                    
                }
                i++;
            }
        }
        catch(Exception e)
        {

        }
        finally
        {
            usersNamesListFromAuthorApi.removeAll(Arrays.asList("", null));
        }
        return usersNamesListFromAuthorApi;
    }
}
