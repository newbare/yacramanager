package fr.wati.yacramanager.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import cn.bluejoe.elfinder.controller.FsException;
import cn.bluejoe.elfinder.controller.executor.CommandExecutionContext;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutorFactory;
import cn.bluejoe.elfinder.service.FsServiceFactory;

@Controller
@RequestMapping("/app/file-connector")
public class ConnectorController
{
	@Resource(name = "commandExecutorFactory")
	private CommandExecutorFactory _commandExecutorFactory;

	@Resource(name = "fsServiceFactory")
	private FsServiceFactory _fsServiceFactory;

	@RequestMapping
	public void connector(HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		try
		{
			request = parseMultipartContent(request);
		}
		catch (Exception e)
		{
			throw new IOException(e.getMessage());
		}

		String cmd = request.getParameter("cmd");
		CommandExecutor ce = _commandExecutorFactory.get(cmd);

		if (ce == null)
		{
			throw new FsException(String.format("unknown command: %s", cmd));
		}

		try
		{
			final HttpServletRequest finalRequest = request;
			ce.execute(new CommandExecutionContext()
			{

				@Override
				public FsServiceFactory getFsServiceFactory()
				{
					return _fsServiceFactory;
				}

				@Override
				public HttpServletRequest getRequest()
				{
					return finalRequest;
				}

				@Override
				public HttpServletResponse getResponse()
				{
					return response;
				}

				@Override
				public ServletContext getServletContext()
				{
					return finalRequest.getSession().getServletContext();
				}
			});
		}
		catch (Exception e)
		{
			throw new FsException("unknown error", e);
		}
	}

	private HttpServletRequest parseMultipartContent(final HttpServletRequest request) throws Exception
	{
		if (!ServletFileUpload.isMultipartContent(request))
			return request;

		final Map<String, String> requestParams = new HashMap<String, String>();
		List<FileItemStream> listFiles = new ArrayList<FileItemStream>();

		StandardMultipartHttpServletRequest multipartHttpServletRequest=null;
		if(request instanceof StandardMultipartHttpServletRequest){
			multipartHttpServletRequest=(StandardMultipartHttpServletRequest) request;
		}else {
			return request;
		}
		Set<Entry<String, List<MultipartFile>>> filesEntrySet = multipartHttpServletRequest.getMultiFileMap().entrySet();
		String characterEncoding = request.getCharacterEncoding();
		if(StringUtils.isEmpty(characterEncoding)) characterEncoding=WebUtils.DEFAULT_CHARACTER_ENCODING;
		for(Entry<String, List<MultipartFile>> entry:filesEntrySet){
			final String fieldName=entry.getKey();
			List<MultipartFile> multipartFiles = entry.getValue();
			for (final MultipartFile multipartFile : multipartFiles) {
				InputStream stream=multipartFile.getInputStream();
				//requestParams.put(multipartFile.getOriginalFilename(), Streams.asString(stream, characterEncoding));
				
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				IOUtils.copy(stream, os);
				final byte[] bs = os.toByteArray();
				stream.close();

				listFiles.add((FileItemStream) Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { FileItemStream.class }, new InvocationHandler()
					{
						@Override
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
						{
							if ("openStream".equals(method.getName()))
							{
								return new ByteArrayInputStream(bs);
							}else if ("getContentType".equals(method.getName())) {
								return multipartFile.getContentType();
							}else if ("getName".equals(method.getName())) {
								return multipartFile.getOriginalFilename();
							}else if ("getFieldName".equals(method.getName())) {
								return multipartFile.getName();
							}else if ("isFormField".equals(method.getName())) {
								return true;
							}

							return method.invoke(multipartFile.getInputStream(), args);
						}
					}));
			}
		}
		
		request.setAttribute(FileItemStream.class.getName(), listFiles);
		return (HttpServletRequest) Proxy.newProxyInstance(this.getClass().getClassLoader(),
			new Class[] { HttpServletRequest.class }, new InvocationHandler()
			{
				@Override
				public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable
				{
					if ("getParameter".equals(arg1.getName()))
					{
						if("cmd".equals(arg2[0])){
							return request.getParameter("cmd");
						}
						if("target".equals(arg2[0])){
							return request.getParameter("target");
						}
						return requestParams.get(arg2[0]);
					}

					return arg1.invoke(request, arg2);
				}
			});
	}
}