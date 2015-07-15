package com.baidu.beidou.navi.codec;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.baidu.beidou.navi.vo.Company;
import com.baidu.beidou.navi.vo.CompanyBuilder;

public abstract class BaseCodecTest {

    protected Codec codec;

    public abstract void initCodec();

    public void encodeAndDecode() throws Exception {
        initCodec();
        Company company = CompanyBuilder.buildSimple();
        byte[] bytes = codec.encode(Company.class, company);
        System.out.println("bytes.length=" + bytes.length);
        Company c = codec.decode(Company.class, bytes);
        System.out.println(c);
        assertThat(c.getId(), is(88));
        assertThat(c.getName(), is("百度时代网络技术(北京)有限公司"));
        assertThat(c.getDepartmentList().size(), is(4));
        assertThat(c.getDepartmentList().get(0).getId(), is(101));
        assertThat(c.getDepartmentList().get(0).getName(), is("R&D"));
    }

}
