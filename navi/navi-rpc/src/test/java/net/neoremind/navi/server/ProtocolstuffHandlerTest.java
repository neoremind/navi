package net.neoremind.navi.server;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import net.neoremind.navi.codec.Codec;
import net.neoremind.navi.codec.protostuff.ProtostuffCodec;
import net.neoremind.navi.constant.NaviStatus;
import net.neoremind.navi.server.handler.NaviRpcHandler;
import net.neoremind.navi.server.handler.protostuff.ProtostuffRpcHandler;
import net.neoremind.navi.server.service.CompanyService;
import net.neoremind.navi.server.vo.NaviRpcRequest;
import net.neoremind.navi.server.vo.NaviRpcResponse;
import net.neoremind.navi.server.vo.RequestDTO;
import net.neoremind.navi.server.vo.ResponseDTO;

public class ProtocolstuffHandlerTest {

	private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Test
	public void testAdd() throws Exception {
		Class<?> companyServiceClass =  Class.forName("net.neoremind.navi.server.service.impl.CompanyServiceImpl");
		CompanyService companyServiceImpl = (CompanyService)(companyServiceClass.newInstance());
		
		Company company = new Company(1000, "脸书-Facebook", dateFormat.parse("1999-06-01"), "Mark", null);
		
		RequestDTO requestDTO = new RequestDTO();
		requestDTO.setId(123456789);
		requestDTO.setMethod("add");
		requestDTO.setParameters(new Object[]{company});
		
		Codec codec = new ProtostuffCodec();
		byte[] protoStuffEncodeByte = codec.encode(RequestDTO.class, requestDTO);
		
		NaviRpcHandler handler = new ProtostuffRpcHandler();
		NaviRpcRequest request = new NaviRpcRequest();
		request.setService(companyServiceClass);
		request.setActor(companyServiceImpl);
		request.setRequest(protoStuffEncodeByte);
		
		NaviRpcResponse response = handler.service(request);

		ResponseDTO responseDTO = codec.decode(ResponseDTO.class, response.getResponse());

		System.out.println("reponse start ---------");
		System.out.println(responseDTO.getId());
		System.out.println(responseDTO.getStatus());
		System.out.println("reponse end ---------");
		assertThat(responseDTO.getId(), is(123456789l));
		assertThat(responseDTO.getStatus(), is(NaviStatus.RPC_OK));
		
		System.out.println("result start ---------");
		Company result = (Company)(responseDTO.getResult());
		System.out.println(result.getId());
		System.out.println(result.getBuildTime());
		System.out.println(result.getCeo());
		System.out.println(result.getName());
		System.out.println(result.getDepartmentNameList());
		System.out.println("result end ---------");
		
		assertThat(result.getId(), is(1000));
		assertThat(result.getBuildTime(), is(dateFormat.parse("1999-06-01")));
		assertThat(result.getCeo(), is("Mark"));
		assertThat(result.getName(), is("脸书-Facebook"));
		assertThat(result.getDepartmentNameList(), nullValue());

	}
	
	@Test
	public void testGetAll() throws Exception {
		Class<?> companyServiceClass =  Class.forName("net.neoremind.navi.server.service.impl.CompanyServiceImpl");
		CompanyService companyServiceImpl = (CompanyService)(companyServiceClass.newInstance());
		
		RequestDTO requestDTO = new RequestDTO();
		requestDTO.setId(123456789);
		requestDTO.setMethod("getAll");
		requestDTO.setParameters(new Object[]{});
		
		Codec codec = new ProtostuffCodec();
		byte[] protoStuffEncodeByte = codec.encode(RequestDTO.class, requestDTO);
		
		NaviRpcHandler handler = new ProtostuffRpcHandler();
		NaviRpcRequest request = new NaviRpcRequest();
		request.setService(companyServiceClass);
		request.setActor(companyServiceImpl);
		request.setRequest(protoStuffEncodeByte);
		
		NaviRpcResponse response = handler.service(request);

		ResponseDTO responseDTO = codec.decode(ResponseDTO.class, response.getResponse());

		System.out.println("reponse start ---------");
		System.out.println(responseDTO.getId());
		System.out.println(responseDTO.getStatus());
		System.out.println("reponse end ---------");
		assertThat(responseDTO.getId(), is(123456789l));
		assertThat(responseDTO.getStatus(), is(NaviStatus.RPC_OK));
		
		System.out.println("result start ---------");
		@SuppressWarnings("unchecked")
		List<Company> list = (List<Company>)(responseDTO.getResult());
		System.out.println(list.size());
		assertThat(list.size(), is(2));

	}
	
	@Test
	public void testGetById() throws Exception {
		Class<?> companyServiceClass =  Class.forName("net.neoremind.navi.server.service.impl.CompanyServiceImpl");
		CompanyService companyServiceImpl = (CompanyService)(companyServiceClass.newInstance());
		
		RequestDTO requestDTO = new RequestDTO();
		requestDTO.setId(123456789);
		requestDTO.setMethod("getById");
		requestDTO.setParameters(new Object[]{1000});
		
		Codec codec = new ProtostuffCodec();
		byte[] protoStuffEncodeByte = codec.encode(RequestDTO.class, requestDTO);
		
		NaviRpcHandler handler = new ProtostuffRpcHandler();
		NaviRpcRequest request = new NaviRpcRequest();
		request.setService(companyServiceClass);
		request.setActor(companyServiceImpl);
		request.setRequest(protoStuffEncodeByte);
		
		NaviRpcResponse response = handler.service(request);

		ResponseDTO responseDTO = codec.decode(ResponseDTO.class, response.getResponse());

		System.out.println("reponse start ---------");
		System.out.println(responseDTO.getId());
		System.out.println(responseDTO.getStatus());
		System.out.println("reponse end ---------");
		assertThat(responseDTO.getId(), is(123456789l));
		assertThat(responseDTO.getStatus(), is(NaviStatus.RPC_OK));
		
		System.out.println("result start ---------");
		Company result = (Company)(responseDTO.getResult());
		System.out.println(result.getId());
		System.out.println(result.getBuildTime());
		System.out.println(result.getCeo());
		System.out.println(result.getName());
		System.out.println(result.getDepartmentNameList());
		System.out.println("result end ---------");
		
		assertThat(result.getId(), is(1000));
		assertThat(result.getName(), is("脸书-Facebook"));

	}
}
