function setupProxy() {
  const tls = process.env.TLS;
  const conf = [
    {
      context: [
        '/api',
        '/data',
        '/upload',
        '/avatar',
        '/jslib',
        '/img',
        '/services',
        '/management',
        '/swagger-resources',
        '/v2/api-docs',
        '/v3/api-docs',
        '/h2-console',
        '/auth',
        '/health',
      ],
      target: `http${tls ? 's' : ''}://localhost:8081`,
      secure: false,
      changeOrigin: tls,
    },
  ];
  return conf;
}

module.exports = setupProxy();
