package io.flutter.plugins.videoplayer;

import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xbill.DNS.DClass;
import org.xbill.DNS.DohResolver;
import org.xbill.DNS.Flags;
import org.xbill.DNS.Header;
import org.xbill.DNS.InvalidTypeException;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.RRSIGRecord;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.ResolverConfig;
import org.xbill.DNS.Section;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;
import org.xbill.DNS.dnssec.ValidatingResolver;
import okhttp3.Dns;

/**
 * Created 2025/1/3 18:58
 * Author:charcolee
 * Version:V1.0
 * ----------------------------------------------------
 * 文件描述：
 * ----------------------------------------------------
 */
public class OkHttpDns implements Dns {
    private static final Dns SYSTEM = Dns.SYSTEM;

    private static final String regex = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b";

    private String dns;

    public void setDNS(String dns) {
        this.dns = dns;
    }

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {

        Log.e("HttpDns", "lookup:" + hostname + " dns:" + dns);

        if(dns == null || dns.isEmpty()) {
            return SYSTEM.lookup(hostname);
        }

        Resolver resolver = new SimpleResolver(dns);

        Record question_record = null;
        try {

            if (!hostname.endsWith(".")) {
                hostname = hostname + ".";
            }

            Name name = Name.fromString(hostname);
            question_record = Record.newRecord(name, 1, 1);
            Message query = Message.newQuery(question_record);
            Message response = resolver.send(query);
            String dnsRecord = rrSetsToString(response.getSectionRRsets(Section.ANSWER));
            // 正则表达式用于提取IP地址
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(dnsRecord);

            // 如果找到匹配的 IP 地址
            if (matcher.find()) {
                String ipAddress = matcher.group();
                Log.d("HttpDns","Extracted IP Address: " + ipAddress);
                return List.of(InetAddress.getByName(ipAddress));
            }
            // 调用HTTPDNS服务获取IP地址

        } catch (Exception e) {
            e.printStackTrace();
        }

        // String ip = HttpDnsHelper.getIpByHost(hostname);
        // if (ip != null && !ip.isEmpty()) {
        //     return Arrays.asList(InetAddress.getAllByName(ip));
        // }
        // // 如果HTTPDNS失败，则使用系统DNS
        return SYSTEM.lookup(hostname);
    }

    private String rrSetsToString(List<RRset> rrsets) {
        StringBuffer ansBuffer = new StringBuffer();
        Iterator it;
        int i;

        for (RRset rrset : rrsets) {
            for (Record r : rrset.rrs()) {
                ansBuffer.append(r.toString());
                ansBuffer.append("\n");
            }

            //RRSIGs
            for (RRSIGRecord sigRec : rrset.sigs())
                ansBuffer.append(sigRec.toString());
            ansBuffer.append("\n");

        }
        //replace tabs
        String ret = ansBuffer.toString().replace('\t', ' ');
        return ret;
    }


}