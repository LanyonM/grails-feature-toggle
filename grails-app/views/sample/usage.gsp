
<p>Tag:           Toggle state is: <g:hasFeature feature="gsp"/><p>
<p>Interpolation: Toggle state is: ${g.hasFeature(feature: "gsp")}<p>
<p>Equals:        Toggle state is: ${g.hasFeature(feature: "gsp") == true}<p>
<p>If:            Toggle state is: 
  <g:if test="${g.hasFeature(feature: "gsp")}">
    true
  </g:if>
  <g:else>
    false
  </g:else>
<p>
<p>If Equals:     Toggle state is: 
  <g:if test="${g.hasFeature(feature: "gsp") == true}">
    true
  </g:if>
  <g:else>
    false
  </g:else>
<p>
<p>With Feature:  Toggle state is: 
  <g:withFeature feature="gsp">
    true
  </g:withFeature>
  <g:withoutFeature>
    false
  </g:withoutFeature>
<p>
