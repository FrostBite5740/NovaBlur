#version 120

uniform sampler2D DiffuseSampler;
uniform sampler2D PrevSampler;

uniform float BlurStrength;

varying vec2 texCoord;

void main() {
    vec3 current = texture2D(DiffuseSampler, texCoord).rgb;
    vec3 previous = texture2D(PrevSampler, texCoord).rgb;
    gl_FragColor = vec4(mix(current, previous, BlurStrength), 1.0);
}
