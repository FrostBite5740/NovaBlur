#version 120

uniform sampler2D DiffuseSampler;

uniform vec2 BlurDir;
uniform float Radius;

varying vec2 texCoord;
varying vec2 oneTexel;

void main() {
    // never more than about 17 samples per pass, so a stronger blur costs no extra frame time
    float stride = max(1.0, Radius / 8.0);
    float sigma = max(Radius * 0.5, 0.5);

    vec3 sum = vec3(0.0);
    float total = 0.0;
    for (float r = -Radius; r <= Radius; r += stride) {
        float w = exp(-(r * r) / (2.0 * sigma * sigma));
        sum += texture2D(DiffuseSampler, texCoord + oneTexel * r * BlurDir).rgb * w;
        total += w;
    }

    gl_FragColor = vec4(sum / total, 1.0);
}
