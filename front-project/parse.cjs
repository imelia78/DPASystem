const http = require('http');
const fs = require('fs');

http.get('http://localhost:8080/v3/api-docs', (res) => {
  let data = '';
  res.on('data', (chunk) => { data += chunk; });
  res.on('end', () => {
    const swagger = JSON.parse(data);
    let output = '';
    for (const [path, methods] of Object.entries(swagger.paths)) {
      for (const [method, details] of Object.entries(methods)) {
        output += `\n=== ${method.toUpperCase()} ${path} ===\n`;
        if (details.parameters) {
          output += 'Params: ' + details.parameters.map(p => p.name + ' (' + p.in + ')').join(', ') + '\n';
        }
        if (details.requestBody && details.requestBody.content) {
          const content = details.requestBody.content['application/json'];
          if (content && content.schema && content.schema.$ref) {
            output += 'Body: ' + content.schema.$ref.split('/').pop() + '\n';
          }
        }
        const resp200 = details.responses['200'] || details.responses['201'];
        if (resp200 && resp200.content && (resp200.content['application/json'] || resp200.content['*/*'])) {
          const content = resp200.content['application/json'] || resp200.content['*/*'];
          if (content && content.schema) {
            if (content.schema.type === 'array' && content.schema.items.$ref) {
              output += 'Response: Array of ' + content.schema.items.$ref.split('/').pop() + '\n';
            } else if (content.schema.$ref) {
              output += 'Response: ' + content.schema.$ref.split('/').pop() + '\n';
            } else {
              output += 'Response: ' + content.schema.type + '\n';
            }
          }
        }
      }
    }
    output += '\n\n=== SCHEMAS ===\n';
    for(const [name, schema] of Object.entries(swagger.components.schemas)) {
       output += name + ': ' + Object.keys(schema.properties || {}).join(', ') + '\n';
    }
    fs.writeFileSync('swagger_summary.txt', output);
    console.log('Done parsing to swagger_summary.txt');
  });
});
