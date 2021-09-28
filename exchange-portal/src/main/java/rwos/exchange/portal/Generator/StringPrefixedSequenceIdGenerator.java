package rwos.exchange.portal.Generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class StringPrefixedSequenceIdGenerator implements IdentifierGenerator{
	@Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
    	final String prefix = "M_";
        Connection conn = session.connection();
        try {
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery("select COUNT(*) + 1 from menu");
            while(rs.next()){
                return prefix + String.format("%03d", rs.getInt(1));
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        
        return null;
    }
}
