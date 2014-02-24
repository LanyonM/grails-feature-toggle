<html>
  <head>
    <title>Site Information</title>
  </head>
<body>
  <table id="features">
    <thead>
      <tr>
        <th>${ncv.msg(code: "pages.admin.featureToggle.feature")}</th>
        <th>${ncv.msg(code: "terms.description")}</th>
        <th colspan="2">${ncv.msg(code: "pages.admin.featureToggle.enableDisable")}</th>
      </tr>
    </thead>
    <tbody>
      <g:each in="${features.keySet().sort({ a, b -> a?.name <=> b?.name})}" var="feature">
        <tr>
          <td>${feature}</td>
          <td>${features[feature].description}</td>
          <g:withFeature feature="${feature}">
            <td>
              <b>${ncv.msg(code: "pages.admin.featureToggle.enabled")}</b>
            </td>
            <td>
              <g:link mapping="disableToggle" params="[feature: feature]">${ncv.msg(code: "pages.admin.featureToggle.disable")}</g:link>
            </td>
          </g:withFeature>
          <g:withoutFeature>
            <td>
              <g:link mapping="enableToggle" params="[feature: feature]">${ncv.msg(code: "pages.admin.featureToggle.enable")}</g:link>
            </td>
            <td>
              <b>${ncv.msg(code: "pages.admin.featureToggle.disabled")}</b>
            </td>
          </g:withoutFeature>
        </tr>
      </g:each>
    </tbody>
  </table>
	<div>
		<a href="${createLink(controller: 'featureToggle', action: 'download')}">
      Download Current Config
    </a>
	</div>
</body>
</html>