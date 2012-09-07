package monitoring.checks;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import monitoring.CheckFailedException;

public class DNSCheck extends AbstractCheck {

    private final String fqdn;
    private final String recordType;
    private final String[] expectedResults;

    public DNSCheck(String fqdn, String recordType, String[] expectedResults) {
        this.fqdn = fqdn;
        this.recordType = recordType;
        this.expectedResults = expectedResults;
    }

    @Override
    public void check() throws CheckFailedException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("java.naming.provider.url", "dns:");
        try {
            DirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(this.fqdn, new String[] {this.recordType});
            for (Enumeration<?> e = attrs.getAll(); e.hasMoreElements();) {
                Attribute a = (Attribute) e.nextElement();
                int size = a.size();
                if (a.size() != this.expectedResults.length) {
                    throw new CheckFailedException("Expected " + this.expectedResults.length + " DNS results for " + this.fqdn + " but got " + a.size());
                }
                List<String> records = new ArrayList<String>();
                for (int i = 0; i < size; i++) {
                    records.add((String) a.get(i));
                }
                checkRecords(records, this.expectedResults);
            }
        } catch (NamingException e) {
            markDown(e);
        }
    }

    private void checkRecords(List<String> records, @SuppressWarnings("hiding") String[] expectedResults) throws CheckFailedException {
        for (String expectedResult : expectedResults) {
            if (!records.contains(expectedResult)) {
                throw new CheckFailedException("Expected record " + expectedResult + " for " + this.fqdn + " missing");
            }
        }
    }
}
